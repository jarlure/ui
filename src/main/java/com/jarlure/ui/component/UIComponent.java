package com.jarlure.ui.component;

public interface UIComponent {

    public static final String VIEW = "view";//通过get(UIComponent.VIEW)获取组件的视图
    public static final String NAME = "name";//通过get(UIComponent.NAME)获取组件的名称

    /**
     * 获取当前UI组件的深度值（z轴坐标值）。深度值决定了组件间的层叠关系，
     * 例如深度值为1的组件总是在深度值为0的组件前边，因此会遮挡住深度值为0的组件
     * @return  当前UI组件的深度值（z轴坐标值）
     */
    float getDepth();

    /**
     * 设置当前UI组件的深度值（z轴坐标值）。深度值决定了组件间的层叠关系，
     * 例如深度值为1的组件总是在深度值为0的组件前边，因此会遮挡住深度值为0的组件
     * @param depth 设置的新的深度值。建议范围在[-10,10]
     */
    void setDepth(float depth);

    /**
     * （以当前尺寸为1的）等比例缩放
     * @param percent   缩放倍数。例如：0.1f即缩小至当前尺寸的十分之一。建议范围在(0，10000)
     */
    default void scale(float percent){
        scale(percent,percent);
    }

    /**
     * （以当前尺寸为1的）非等比例缩放
     * @param x_percent 宽度缩放倍数。例如：0.1f即宽度缩小至当前宽度的十分之一。建议范围在(0，10000)
     * @param y_percent 高度缩放倍数。例如：0.1f即高度缩小至当前高度的十分之一。建议范围在(0，10000)
     */
    void scale(float x_percent,float y_percent);

    /**
     * （以当前位置的世界坐标为准的）移动
     * @param dx    水平移动。dx大于0组件右移；dx小于0组件左移
     * @param dy    垂直移动。dy大于0组件上移；dy小于0组件下移
     */
    void move(float dx,float dy);

    /**
     * 以自身原点为轴点，绕z轴旋转
     * @param angle 旋转角度，弧度。angle大于0组件向右旋转；angle小于0组件向左旋转
     */
    void rotate(float angle);

    /**
     * 当前组件是否是可见的
     * @return  当前组件是否可见
     */
    boolean isVisible();

    /**
     * 设置当前组件是否可见。可以通过设置false隐藏该组件
     * @param visible
     */
    void setVisible(boolean visible);

    /**
     * 设置当前组件是否可见。如果当前组件可见，则隐藏它；如果当前组件不可见，则显示它。相当于setVisible(!isVisible());
     * @return  设置后当前组件是否可见。相当于调用这个方法后再调用一次isVisible();
     */
    boolean toggleVisible();

    /**
     * 获取当前组件的某个参数。该方法一般用于获取当前组件的成员变量
     * @param param 参数名
     * @return
     */
    Object get(String param);

    /**
     * 判断当前组件是否有某个类型的数据
     * @param type
     * @param <T>
     * @return
     */
    <T> boolean exist(Class<T> type);

    /**
     * 获取与当前组件相关联的某个数据。如果当前组件没有该类型的数据，则会自动创建一个该类型的实例。
     * 如果该类型无法通过反射机制动态创建，则会返回null
     * @param type  该数据的类型
     * @param <T>
     * @return  与当前组件相关联的类型为type的数据
     */
    <T> T get(Class<T> type);

    /**
     * 给当前组件设置关联数据。如果当前组件已存在该类型的数据，则会用新数据替换掉旧数据。
     * @param type  该数据的类型
     * @param value 要关联的数据。这个数据的类型必须是type的子类
     * @param <K>
     * @param <V>
     */
    <K,V extends K>void set(Class<K> type,V value);

}
