package com.beicai.xiaoyuanzhibo.bean;

import java.io.Serializable;

/**
 * Created by xiaoyuan on 17/3/15.
 */

public class UserBean implements Serializable {

    /**
     * created_at : 1489044543163
     * updated_at : 1489044543225
     * id : 1157677280460801
     * user_data : {"phone":"18622881126","user_name":"小源","avatar":"http://d.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=44efbe491e30e924cff194377c38423e/dcc451da81cb39dbf51ac417d1160924aa18309c.jpg"}
     */

    private ResultBean result;
    /**
     * result : {"created_at":1489044543163,"updated_at":1489044543225,"id":1157677280460801,"user_data":{"phone":"18622881126","user_name":"小源","avatar":"http://d.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=44efbe491e30e924cff194377c38423e/dcc451da81cb39dbf51ac417d1160924aa18309c.jpg"}}
     * error_code : 0
     */

    private int error_code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        private long created_at;
        private long updated_at;
        private long id;
        /**
         * phone : 18622881126
         * user_name : 小源
         * avatar : http://d.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=44efbe491e30e924cff194377c38423e/dcc451da81cb39dbf51ac417d1160924aa18309c.jpg
         */

        private UserDataBean user_data;

        public long getCreated_at() {
            return created_at;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }

        public long getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(long updated_at) {
            this.updated_at = updated_at;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public UserDataBean getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBean user_data) {
            this.user_data = user_data;
        }

        public static class UserDataBean {
            private String phone;
            private String user_name;
            private String avatar;

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
