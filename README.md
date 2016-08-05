### 效果：
![step0.png](art/step0.png)
![step1.png](art/step1.png)


![step2.png](art/step2.png)
![step3.png](art/step3.png)

### 使用说明
#### 添加schemas:

    xmlns:app="http://schemas.android.com/apk/res-auto"

#### 引用控件：

    <com.bd.favorite.FavoritePicker
      app:mode="fill"
      android:layout_alignParentBottom="true"
      android:id="@+id/picker"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      />

#### 调用方式
参考demo app：

    FavoritePicker picker= (FavoritePicker) findViewById(R.id.picker);
    /*设置栅格总数*/    
    picker.setLimitFavo(5);
    /*设置栅格间距*/
    picker.setSpace(5);
    /*设置栅格列数*/
    picker.setColumnNumber(3);
    List<Favo> favos = new ArrayList<>();
    favos.add(new Favo(0,"第一张","http://download.easyicon.net/png/1082117/128/"));
    favos.add(new Favo(0,"第二张","http://download.easyicon.net/png/1082114/128/"));
    favos.add(new Favo(0,"第三张","http://download.easyicon.net/png/1082113/128/"));
    favos.add(new Favo(0,"第四张","http://download.easyicon.net/png/1082115/128/"));
    picker.bind(favos);
