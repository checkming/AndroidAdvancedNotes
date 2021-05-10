/*
 * Copyright (C) 2005 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef ANDROID_REF_BASE_H
#define ANDROID_REF_BASE_H
// Location: external/libcxx/include
// C++11原子操作
#include <atomic>
// Location: bionic/libc/include
#include <stdint.h>
#include <sys/types.h>
#include <stdlib.h>
#include <string.h>
// Locaion: system/core/include
#include <utils/StrongPointer.h>
#include <utils/TypeHelpers.h>

// ---------------------------------------------------------------------------
namespace android {
// Location: system/libhwbinder
// 抽象类
class TextOutput;
TextOutput& printWeakPointer(TextOutput& to, const void* val);

// ---------------------------------------------------------------------------
// 用于wp的宏
// 简化了比较操作符的定义
#define COMPARE_WEAK(_op_)                                      \
inline bool operator _op_ (const sp<T>& o) const {              \
    return m_ptr _op_ o.m_ptr;                                  \
}                                                               \
inline bool operator _op_ (const T* o) const {                  \
    return m_ptr _op_ o;                                        \
}                                                               \
template<typename U>                                            \
inline bool operator _op_ (const sp<U>& o) const {              \
    return m_ptr _op_ o.m_ptr;                                  \
}                                                               \
template<typename U>                                            \
inline bool operator _op_ (const U* o) const {                  \
    return m_ptr _op_ o;                                        \
}

// ---------------------------------------------------------------------------
/** * @class ReferenceRenamer * operator() 纯虚函数 * 非虚析构函数（故意为之）避免派生类的代码开销 */
class ReferenceRenamer {
protected:
    // destructor is purposedly not virtual so we avoid code overhead from
    // subclasses; we have to make it protected to guarantee that it
    // cannot be called from this base class (and to make strict compilers
    // happy).
    ~ReferenceRenamer() { }
public:
    virtual void operator()(size_t i) const = 0;
};

// ---------------------------------------------------------------------------
/** * @class RefBase * 引用计数基类 */
class RefBase
{
public:
            void            incStrong(const void* id) const;// 增加强引用计数
            void            decStrong(const void* id) const;// 减少强引用计数
    
            void            forceIncStrong(const void* id) const;// 强制增加强引用计数

            //! DEBUGGING ONLY: Get current strong ref count.
            int32_t         getStrongCount() const;// 获取当前强引用计数（只用于debug）

    /** * @class weakref_type * 弱引用计数类型 */
    class weakref_type
    {
    public:
        RefBase*            refBase() const;// 获取引用计数基类指针
        
        void                incWeak(const void* id);// 增加弱引用计数
        void                decWeak(const void* id);// 减少弱引用计数
        
        // acquires a strong reference if there is already one.
        bool                attemptIncStrong(const void* id);// 获取强引用
        
        // acquires a weak reference if there is already one.
        // This is not always safe. see ProcessState.cpp and BpBinder.cpp
        // for proper use.
        bool                attemptIncWeak(const void* id); // 获取弱引用（可能不安全）

        //! DEBUGGING ONLY: Get current weak ref count.
        int32_t             getWeakCount() const;// 获取当前弱引用计数（只用于debug）

        //! DEBUGGING ONLY: Print references held on object.
        void                printRefs() const;// 打印引用计数相关信息（只用于debug）

        //! DEBUGGING ONLY: Enable tracking for this object.
        // enable -- enable/disable tracking
        // retain -- when tracking is enable, if true, then we save a stack trace
        //           for each reference and dereference; when retain == false, we
        //           match up references and dereferences and keep only the 
        //           outstanding ones.
         /** * @fn trackMe: 跟踪引用计数信息 * @param enable: true-跟踪 false-不跟踪 
         * @param retain: true-跟踪所有的 false-跟踪最新的 * @note 只用于debug */
        void                trackMe(bool enable, bool retain);
    };
    
            weakref_type*   createWeak(const void* id) const;// 创建弱引用计数类型
            
            weakref_type*   getWeakRefs() const;// 获取弱引用计数类型指针

            //! DEBUGGING ONLY: Print references held on object.
    inline  void            printRefs() const { getWeakRefs()->printRefs(); }// 打印引用计数相关信息（只用于debug）

            //! DEBUGGING ONLY: Enable tracking of object.
    inline  void            trackMe(bool enable, bool retain)
    { 
        getWeakRefs()->trackMe(enable, retain); 
    }

    typedef RefBase basetype;// typedef别名

protected:
                            RefBase();// protected构造（使用时需派生子类）
    virtual                 ~RefBase();// 虚析构
    
    //! Flags for extendObjectLifetime()
     // 用于extendObjectLifetime()
    enum {
        OBJECT_LIFETIME_STRONG  = 0x0000,
        OBJECT_LIFETIME_WEAK    = 0x0001,
        OBJECT_LIFETIME_MASK    = 0x0001
    };
    
            void            extendObjectLifetime(int32_t mode);// 扩展对象生命期
            
    //! Flags for onIncStrongAttempted()
    // 用于 onIncStrongAttempted()
    enum {
        FIRST_INC_STRONG = 0x0001
    };
     /** * 四个回调 * @fn onFirstRef: 强指针/引用初始化后回调 * @fn onLastStrongRef: 最后一个强引用不再使用时或者需要取消不必要的onIncStrongAttempted的影响时 
     * @fn onIncStrongAttempted: 生命期为OBJECT_LIFETIME_WEAK 
     * @return true-成功转化为强指针时（可能有副作用） 
     × @param flags: [TO-BE-REMOVED]FIRST_INC_STRONG 
     * @fn onLastWeakRef: [TO-BE-REMOVED]生命期为OBJECT_LIFETIME_WEAK且最后一个引用不再使用时 */
    virtual void            onFirstRef();
    virtual void            onLastStrongRef(const void* id);
    virtual bool            onIncStrongAttempted(uint32_t flags, const void* id);
    virtual void            onLastWeakRef(const void* id);

private:
    friend class weakref_type;// 友元
    class weakref_impl;// RefBase.cpp中定义
    
                            RefBase(const RefBase& o);// 拷贝构造（无定义-禁止拷贝）
            RefBase&        operator=(const RefBase& o);// 赋值操作符（无定义-禁止赋值）

private:
    friend class ReferenceMover;// 下面有定义

    // 三个用于修改引用属性的静态函数
    static void renameRefs(size_t n, const ReferenceRenamer& renamer);

    static void renameRefId(weakref_type* ref,
            const void* old_id, const void* new_id);

    static void renameRefId(RefBase* ref,
            const void* old_id, const void* new_id);

        weakref_impl* const mRefs;// 唯一的成员变量（定义隐藏在cpp中为常说的句柄）
};

// ---------------------------------------------------------------------------
/** * @class LightRefBase<T> * 轻量级的RefBase（少了不少多西） * 原子操作使用了C++11 */
template <class T>
class LightRefBase
{
public:
    inline LightRefBase() : mCount(0) { }
    inline void incStrong(__attribute__((unused)) const void* id) const {
        mCount.fetch_add(1, std::memory_order_relaxed);
    }
    inline void decStrong(__attribute__((unused)) const void* id) const {
        if (mCount.fetch_sub(1, std::memory_order_release) == 1) {
            std::atomic_thread_fence(std::memory_order_acquire);
            delete static_cast<const T*>(this);
        }
    }
    //! DEBUGGING ONLY: Get current strong ref count.
    inline int32_t getStrongCount() const {// 只用于debug
        return mCount.load(std::memory_order_relaxed);
    }

    typedef LightRefBase<T> basetype;

protected:
    inline ~LightRefBase() { }

private:
    friend class ReferenceMover;
    inline static void renameRefs(size_t n, const ReferenceRenamer& renamer) { }
    inline static void renameRefId(T* ref,
            const void* old_id, const void* new_id) { }

private:
    mutable std::atomic<int32_t> mCount;
};

// This is a wrapper around LightRefBase that simply enforces a virtual
// destructor to eliminate the template requirement of LightRefBase
/** * @class VirtualLightRefBase * 使用LightRefBase进行包装 * 
只是声明了虚析构函数以消除LightRefBase的模板需求 */
class VirtualLightRefBase : public LightRefBase<VirtualLightRefBase> {
public:
    virtual ~VirtualLightRefBase() {}
};

// ---------------------------------------------------------------------------
/** * @class wp<T> */
template <typename T>
class wp
{
public:
     // 模板中经常使用typename声明其后的内容为一种数据类型
    typedef typename RefBase::weakref_type weakref_type;
    // 构造
    inline wp() : m_ptr(0) { }
    // （拷贝）构造
    wp(T* other);
    wp(const wp<T>& other);
    wp(const sp<T>& other);
    template<typename U> wp(U* other);
    template<typename U> wp(const sp<U>& other);
    template<typename U> wp(const wp<U>& other);
    // 析构
    ~wp();
    
    // Assignment
 // 赋值操作符
    wp& operator = (T* other);
    wp& operator = (const wp<T>& other);
    wp& operator = (const sp<T>& other);
    
    template<typename U> wp& operator = (U* other);
    template<typename U> wp& operator = (const wp<U>& other);
    template<typename U> wp& operator = (const sp<U>& other);
     // 同时设置对象和引用
    void set_object_and_refs(T* other, weakref_type* refs);

    // promotion to sp
    // 转换为强指针
    sp<T> promote() const;

    // Reset
    // 清空wp保存的对象指针
    void clear();

    // Accessors
     // 获取弱引用类型指针/获取wp保存的对象指针
    inline  weakref_type* get_refs() const { return m_refs; }
    
    inline  T* unsafe_get() const { return m_ptr; }

    // Operators
    // 比较操作符
    COMPARE_WEAK(==)
    COMPARE_WEAK(!=)
    COMPARE_WEAK(>)
    COMPARE_WEAK(<)
    COMPARE_WEAK(<=)
    COMPARE_WEAK(>=)

    inline bool operator == (const wp<T>& o) const {
        return (m_ptr == o.m_ptr) && (m_refs == o.m_refs);
    }
    template<typename U>
    inline bool operator == (const wp<U>& o) const {
        return m_ptr == o.m_ptr;
    }

    inline bool operator > (const wp<T>& o) const {
        return (m_ptr == o.m_ptr) ? (m_refs > o.m_refs) : (m_ptr > o.m_ptr);
    }
    template<typename U>
    inline bool operator > (const wp<U>& o) const {
        return (m_ptr == o.m_ptr) ? (m_refs > o.m_refs) : (m_ptr > o.m_ptr);
    }

    inline bool operator < (const wp<T>& o) const {
        return (m_ptr == o.m_ptr) ? (m_refs < o.m_refs) : (m_ptr < o.m_ptr);
    }
    template<typename U>
    inline bool operator < (const wp<U>& o) const {
        return (m_ptr == o.m_ptr) ? (m_refs < o.m_refs) : (m_ptr < o.m_ptr);
    }
                         inline bool operator != (const wp<T>& o) const { return m_refs != o.m_refs; }
    template<typename U> inline bool operator != (const wp<U>& o) const { return !operator == (o); }
                         inline bool operator <= (const wp<T>& o) const { return !operator > (o); }
    template<typename U> inline bool operator <= (const wp<U>& o) const { return !operator > (o); }
                         inline bool operator >= (const wp<T>& o) const { return !operator < (o); }
    template<typename U> inline bool operator >= (const wp<U>& o) const { return !operator < (o); }

private:
    // （其它的）sp<>和wp<>为友元
    template<typename Y> friend class sp;
    template<typename Y> friend class wp;
 // wp保存的对象指针和对应的弱引用类型指针
    T*              m_ptr;
    weakref_type*   m_refs;
};
// 为TextOutput重载operator<<
template <typename T>
TextOutput& operator<<(TextOutput& to, const wp<T>& val);

#undef COMPARE_WEAK

// ---------------------------------------------------------------------------
// No user serviceable parts below here.
// 下面是（拷贝）构造的几种形式
// 需要注意若引用更新
template<typename T>
wp<T>::wp(T* other)
    : m_ptr(other)
{
    if (other) m_refs = other->createWeak(this);
}

template<typename T>
wp<T>::wp(const wp<T>& other)
    : m_ptr(other.m_ptr), m_refs(other.m_refs)
{
    if (m_ptr) m_refs->incWeak(this);
}

template<typename T>
wp<T>::wp(const sp<T>& other)
    : m_ptr(other.m_ptr)
{
    if (m_ptr) {
        m_refs = m_ptr->createWeak(this);
    }
}

template<typename T> template<typename U>
wp<T>::wp(U* other)
    : m_ptr(other)
{
    if (other) m_refs = other->createWeak(this);
}

template<typename T> template<typename U>
wp<T>::wp(const wp<U>& other)
    : m_ptr(other.m_ptr)
{
    if (m_ptr) {
        m_refs = other.m_refs;
        m_refs->incWeak(this);
    }
}

template<typename T> template<typename U>
wp<T>::wp(const sp<U>& other)
    : m_ptr(other.m_ptr)
{
    if (m_ptr) {
        m_refs = m_ptr->createWeak(this);
    }
}

template<typename T>
wp<T>::~wp()
{
    if (m_ptr) m_refs->decWeak(this);
}
// 析构函数减少弱引用但不直接delete对象指针m_ptr
// 在decWeak中当若引用减少到1时自动delete
template<typename T>
wp<T>& wp<T>::operator = (T* other)
{
    weakref_type* newRefs =
        other ? other->createWeak(this) : 0;
    if (m_ptr) m_refs->decWeak(this);
    m_ptr = other;
    m_refs = newRefs;
    return *this;
}
// 下面是重载的operator =的几种形式
// 需要注意新、旧对象的弱引用更新
template<typename T>
wp<T>& wp<T>::operator = (const wp<T>& other)
{
    weakref_type* otherRefs(other.m_refs);
    T* otherPtr(other.m_ptr);
    if (otherPtr) otherRefs->incWeak(this);
    if (m_ptr) m_refs->decWeak(this);
    m_ptr = otherPtr;
    m_refs = otherRefs;
    return *this;
}

template<typename T>
wp<T>& wp<T>::operator = (const sp<T>& other)
{
    weakref_type* newRefs =
        other != NULL ? other->createWeak(this) : 0;
    T* otherPtr(other.m_ptr);
    if (m_ptr) m_refs->decWeak(this);
    m_ptr = otherPtr;
    m_refs = newRefs;
    return *this;
}

template<typename T> template<typename U>
wp<T>& wp<T>::operator = (U* other)
{
    weakref_type* newRefs =
        other ? other->createWeak(this) : 0;
    if (m_ptr) m_refs->decWeak(this);
    m_ptr = other;
    m_refs = newRefs;
    return *this;
}

template<typename T> template<typename U>
wp<T>& wp<T>::operator = (const wp<U>& other)
{
    weakref_type* otherRefs(other.m_refs);
    U* otherPtr(other.m_ptr);
    if (otherPtr) otherRefs->incWeak(this);
    if (m_ptr) m_refs->decWeak(this);
    m_ptr = otherPtr;
    m_refs = otherRefs;
    return *this;
}

template<typename T> template<typename U>
wp<T>& wp<T>::operator = (const sp<U>& other)
{
    weakref_type* newRefs =
        other != NULL ? other->createWeak(this) : 0;
    U* otherPtr(other.m_ptr);
    if (m_ptr) m_refs->decWeak(this);
    m_ptr = otherPtr;
    m_refs = newRefs;
    return *this;
}
// 保存对象指针和若引用类型指针时
// 需要对other和m_ptr分别进行减少、增加弱引用处理
template<typename T>
void wp<T>::set_object_and_refs(T* other, weakref_type* refs)
{
    if (other) refs->incWeak(this);
    if (m_ptr) m_refs->decWeak(this);
    m_ptr = other;
    m_refs = refs;
}
// 把对象指针m_ptr提升为强指针
template<typename T>
sp<T> wp<T>::promote() const
{
    sp<T> result;
    if (m_ptr && m_refs->attemptIncStrong(&result)) {
        result.set_pointer(m_ptr);
    }
    return result;
}
// 清空对象指针m_ptr并减少弱引用
template<typename T>
void wp<T>::clear()
{
    if (m_ptr) {
        m_refs->decWeak(this);
        m_ptr = 0;
    }
}
// 重载了TextOutput的operator<<
template <typename T>
inline TextOutput& operator<<(TextOutput& to, const wp<T>& val)
{
    return printWeakPointer(to, val.unsafe_get());
}

// ---------------------------------------------------------------------------

// this class just serves as a namespace so TYPE::moveReferences can stay
// private.
class ReferenceMover {
public:
    // it would be nice if we could make sure no extra code is generated
    // for sp<TYPE> or wp<TYPE> when TYPE is a descendant of RefBase:
    // Using a sp<RefBase> override doesn't work; it's a bit like we wanted
    // a template<typename TYPE inherits RefBase> template...

    template<typename TYPE> static inline
    void move_references(sp<TYPE>* d, sp<TYPE> const* s, size_t n) {

        class Renamer : public ReferenceRenamer {
            sp<TYPE>* d;
            sp<TYPE> const* s;
            virtual void operator()(size_t i) const {
                // The id are known to be the sp<>'s this pointer
                TYPE::renameRefId(d[i].get(), &s[i], &d[i]);
            }
        public:
            Renamer(sp<TYPE>* d, sp<TYPE> const* s) : d(d), s(s) { }
            virtual ~Renamer() { }
        };

        memmove(d, s, n*sizeof(sp<TYPE>));
        TYPE::renameRefs(n, Renamer(d, s));
    }


    template<typename TYPE> static inline
    void move_references(wp<TYPE>* d, wp<TYPE> const* s, size_t n) {

        class Renamer : public ReferenceRenamer {
            wp<TYPE>* d;
            wp<TYPE> const* s;
            virtual void operator()(size_t i) const {
                // The id are known to be the wp<>'s this pointer
                TYPE::renameRefId(d[i].get_refs(), &s[i], &d[i]);
            }
        public:
            Renamer(wp<TYPE>* d, wp<TYPE> const* s) : d(d), s(s) { }
            virtual ~Renamer() { }
        };

        memmove(d, s, n*sizeof(wp<TYPE>));
        TYPE::renameRefs(n, Renamer(d, s));
    }
};

// specialization for moving sp<> and wp<> types.
// these are used by the [Sorted|Keyed]Vector<> implementations
// sp<> and wp<> need to be handled specially, because they do not
// have trivial copy operation in the general case (see RefBase.cpp
// when DEBUG ops are enabled), but can be implemented very
// efficiently in most cases.

template<typename TYPE> inline
void move_forward_type(sp<TYPE>* d, sp<TYPE> const* s, size_t n) {
    ReferenceMover::move_references(d, s, n);
}

template<typename TYPE> inline
void move_backward_type(sp<TYPE>* d, sp<TYPE> const* s, size_t n) {
    ReferenceMover::move_references(d, s, n);
}

template<typename TYPE> inline
void move_forward_type(wp<TYPE>* d, wp<TYPE> const* s, size_t n) {
    ReferenceMover::move_references(d, s, n);
}

template<typename TYPE> inline
void move_backward_type(wp<TYPE>* d, wp<TYPE> const* s, size_t n) {
    ReferenceMover::move_references(d, s, n);
}


}; // namespace android

// ---------------------------------------------------------------------------

#endif // ANDROID_REF_BASE_H
