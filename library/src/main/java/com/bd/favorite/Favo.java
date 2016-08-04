package com.bd.favorite;

/**
 * Description : <Content><br>
 * CreateTime : 2016/8/4 15:48
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/8/4 15:48
 * @ModifyDescription : <Content>
 */
public class Favo {
    long _id;
    String name;
    String ImageUrl;

    public Favo(long _id) {
        this._id = _id;
    }

    public Favo(long _id, String name, String imageUrl) {
        this._id = _id;
        this.name = name;
        ImageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Favo{" +
                "name='" + name + '\'' +
                ", ImageUrl='" + ImageUrl + '\'' +
                '}';
    }
}
