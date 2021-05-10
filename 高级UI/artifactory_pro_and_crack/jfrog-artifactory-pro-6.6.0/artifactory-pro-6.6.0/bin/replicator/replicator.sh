#!/bin/bash
#
# Startup script for Artifactory Replicator
#

exitError() {
    echo -e "\n\033[31mERROR: $1\033[0m\n"

    [ -z "${2}" ] || usage
    exit 1
}

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SCRIPT_NAME=$(basename $0)

NAME=Replicator
REPLICATOR_DATA=${REPLICATOR_DATA:-"${SCRIPT_DIR}/../../replicator"}
ARTIFACTORY_LOCAL_URL=http://localhost:8081/artifactory
WAIT_FOR_ARTIFACTORY_LIMIT=180

REPLICATOR_PID=${REPLICATOR_DATA}/run/replicator.pid
REPLICATOR_LOG=${REPLICATOR_DATA}/logs/replicator.log
REPLICATOR_BIN=

usage() {
    cat << END_USAGE

${SCRIPT_NAME} - script for controlling ${NAME}

Usage: ./${SCRIPT_NAME} <action>

action:    help|start|stop|restart|status


END_USAGE

    exit 1
}


# Process command line options. See usage above for supported options
processOptions () {
    # Get action - help|start|stop|restart|status
    ACTION=${1}
    shift

    if [[ ! "${ACTION}" =~ ^(help|start|stop|restart|status)$ ]]; then
        usage
    fi

    if [[ ! "${ACTION}" =~ ^(help|start|stop|restart|status)$ ]]; then
        usage
    fi

    if [[ "${ACTION}" =~ ^help$ ]]; then
        usage
    fi

    # Process the options
    while [[ $# > 0 ]]; do
        case "$1" in
            *)
                usage
            ;;
        esac
    done

    # On start, make sure options are passed
    if [ "${ACTION}" == "start" ]; then
        REPLICATOR_PARAMS="--replicatorHome=${REPLICATOR_DATA}"
    fi
}

setExecutable() {
    local os=$(uname -s)
    local arch=$(uname -m)

    # Resolve OS
    case ${os} in
        Linux)
            os=linux
        ;;
        Darwin)
            os=darwin
        ;;
        *)
            exitError "OS type ${os} is not supported"
        ;;
    esac

    # Resolve cpu Architecture
    case ${arch} in
        x86_64)
            arch=amd64
        ;;
        *)
            exitError "CPU architecture ${arch} is not supported"
        ;;
    esac

    REPLICATOR_BIN=${SCRIPT_DIR}/replicator-${os}-${arch}

}

isRunning() {
    local ps=
    # Set binary to run
    setExecutable
    local bin=${REPLICATOR_BIN}
    # Check running state by PID
    if [ -e "${REPLICATOR_PID}" ]; then
        PID_VALUE=$(cat ${REPLICATOR_PID})
        if [ -n "${PID_VALUE}" ]; then
            ps=$(pidof ${bin})
        fi
    else
        # Try and find by process
        ps=$(ps -ef | grep -i ${bin} | grep -v grep 2> /dev/null)
        PID_VALUE=$(echo -n ${ps} | awk '{print $2}')
    fi
    if [ -z "${ps}" ]; then
        if [ -z "${PID_VALUE}" ]; then
            return 1 # Not running
        else
            return 2 # PID exists but process dead
        fi
    else
        return 0 # Is running
    fi
}


start() {
    echo "Starting ${NAME}..."

    # Check if running
    isRunning
    case $? in
        0)
            echo "${NAME} is already running (PID: ${PID_VALUE})"
            restart
            exit 0
        ;;
        1)
#            echo "${NAME} not running. Proceed to start it up."
        ;;
        2)
            echo "PID exists (${PID_VALUE}), but ${NAME} not running. Proceed to start it up."
        ;;
        *)
            exitError "Function isRunning() returned an unknown value ($?)"
        ;;
    esac

    # Start replicator
    mkdir -p ${REPLICATOR_DATA}/logs
    mkdir -p ${REPLICATOR_DATA}/run
    nohup ${REPLICATOR_BIN} ${REPLICATOR_PARAMS} 2>&1 &
    local pid=$!
    echo -n ${pid} > ${REPLICATOR_PID}

    echo "${NAME} started. PID: ${pid}"
}

stop() {
    echo "Stopping ${NAME}..."

    # Check if running
    isRunning
    case $? in
        0)
            echo "${NAME} is running (PID: ${PID_VALUE}). Stopping it..."
        ;;
        1)
            echo "${NAME} not running."
            exit 0
        ;;
        2)
            echo "PID exists (${PID_VALUE}), but ${NAME} not running. Removing PID file..."
            rm -f ${REPLICATOR_PID}
            exit 0
        ;;
        *)
            exitError "Function isRunning() returned an unknown value ($?)"
        ;;
    esac

    # Kill using PID
    kill ${PID_VALUE} || exitError "Stopping ${NAME} failed"
    rm -f ${REPLICATOR_PID}
    echo "${NAME} stopped"
}

restart() {
    echo "Restarting ${NAME}..."
    ${SCRIPT_DIR}/${SCRIPT_NAME} stop
    ${SCRIPT_DIR}/${SCRIPT_NAME} start $*
}

status() {
    echo -n "${NAME} is "

    # Check if running
    isRunning
    case $? in
        0)
            echo "running (PID: ${PID_VALUE})"
        ;;
        1)
            echo "not running"
        ;;
        2)
            echo "PID exists (${PID_VALUE}), but ${NAME} not running."
        ;;
        *)
            exitError "Function isRunning() returned an unknown value ($?)"
        ;;
    esac
}

processOptions $*
eval ${ACTION}

