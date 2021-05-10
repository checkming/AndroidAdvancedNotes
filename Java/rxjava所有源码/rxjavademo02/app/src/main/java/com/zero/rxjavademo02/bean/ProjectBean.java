package com.zero.rxjavademo02.bean;

import java.util.List;

public class ProjectBean {


    /**
     * data : [{"children":[],"courseId":13,"id":294,"name":"完整项目","order":145000,"parentChapterId":293,"userControlSetTop":false,"visible":0},{"children":[],"courseId":13,"id":402,"name":"跨平台应用","order":145001,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":367,"name":"资源聚合类","order":145002,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":323,"name":"动画","order":145003,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":314,"name":"RV列表动效","order":145004,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":358,"name":"项目基础功能","order":145005,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":328,"name":"网络&amp;文件下载","order":145011,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":331,"name":"TextView","order":145013,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":336,"name":"键盘","order":145015,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":337,"name":"快应用","order":145016,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":338,"name":"日历&amp;时钟","order":145017,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":339,"name":"K线图","order":145018,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":340,"name":"硬件相关","order":145019,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":357,"name":"表格类","order":145022,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":363,"name":"创意汇","order":145024,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":380,"name":"ImageView","order":145029,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":382,"name":"音视频&amp;相机","order":145030,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":383,"name":"相机","order":145031,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":310,"name":"下拉刷新","order":145032,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":385,"name":"架构","order":145033,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":387,"name":"对话框","order":145035,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":388,"name":"数据库","order":145036,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":391,"name":"AS插件","order":145037,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":400,"name":"ViewPager","order":145039,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":401,"name":"二维码","order":145040,"parentChapterId":293,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":312,"name":"富文本编辑器","order":145041,"parentChapterId":293,"userControlSetTop":false,"visible":1}]
     * errorCode : 0
     * errorMsg :
     */

    private int errorCode;
    private String errorMsg;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "ProjectBean{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * children : []
         * courseId : 13
         * id : 294
         * name : 完整项目
         * order : 145000
         * parentChapterId : 293
         * userControlSetTop : false
         * visible : 0
         */

        private int courseId;
        private int id;
        private String name;
        private int order;
        private int parentChapterId;
        private boolean userControlSetTop;
        private int visible;
        private List<?> children;

        @Override
        public String toString() {
            return "DataBean{" +
                    "courseId=" + courseId +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", order=" + order +
                    ", parentChapterId=" + parentChapterId +
                    ", userControlSetTop=" + userControlSetTop +
                    ", visible=" + visible +
                    ", children=" + children +
                    '}';
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getParentChapterId() {
            return parentChapterId;
        }

        public void setParentChapterId(int parentChapterId) {
            this.parentChapterId = parentChapterId;
        }

        public boolean isUserControlSetTop() {
            return userControlSetTop;
        }

        public void setUserControlSetTop(boolean userControlSetTop) {
            this.userControlSetTop = userControlSetTop;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        public List<?> getChildren() {
            return children;
        }

        public void setChildren(List<?> children) {
            this.children = children;
        }
    }
}
