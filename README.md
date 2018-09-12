# BindexView

![效果图](https://github.com/MrTrying/BindexViewDemo/blob/master/pic/preview.gif?raw=true)

## 使用

##### XML属性使用

xml中提供设置文字选中和未选中颜色，以及选中的背景色或背景Drawable（背景Drawable优先于背景色）。

```xml
<com.mrtrying.bindexlib.BindexNavigationView
    android:id="@+id/bindexNavigationView"
    android:layout_width="20dp"
    android:layout_height="match_parent"
    android:layout_alignParentRight="true"
    android:textSize="10sp"
    app:selectedBackgroundColor="@color/colorPrimary"
    app:selectedBackgroundDrawable="@drawable/bg_select"
    app:unselectedTextColor="@color/colorPrimary"
    app:selectedTextColor="@color/colorAccent"/>
```

##### 设置数据

```java
BindexNavigationView navigationView = findViewById(R.id.bindexNavigationView);
String[] wrods = {"↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z","#"};
ArrayList<BindexNavigationView.IndexBean> indexBeans = new ArrayList<>();
for(String str:wrods){
    indexBeans.add(new BindexNavigationView.IndexBean(str));
}
navigationView.setData(indexBeans);
```

##### 设置回调

```java
navigationView.addOnItemSelectedListener(new BindexNavigationView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(int position, BindexNavigationView.IndexBean bean) {
        textView.setText(bean.getIndexValue());
    }
});
```

## 历史版本

##### V1.0.1
- 添加背景颜色属性
- 添加背景是否圆角属性
- 添加背景高度是否撑满属性

##### V1.0.0
- 文字选中(未选中)颜色
- 选中背景颜色
- 选中背景Drawable
- 选中监听回调

