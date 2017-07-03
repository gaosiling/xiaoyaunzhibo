package com.beicai.xiaoyuanzhibo.bean;

import java.io.Serializable;

/**
 * Created by xiaoyuan on 17/3/15.
 */

public class CreatLiveBean implements Serializable {

    /**
     * created_at : 1489060273398
     * updated_at : 1489060273398
     * id : 1157941186068490
     * data : {"live_type":1,"pic":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489070322042&di=a37c150bc138b0ec5e2c1b749ca0e868&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb151f8198618367abfffecd72c738bd4b31ce542.jpg","live_name":"高龙龙直播"}
     * uid : 1157677280460801
     */

    private ResultBean result;
    /**
     * result : {"created_at":1489060273398,"updated_at":1489060273398,"id":1157941186068490,"data":{"live_type":1,"pic":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489070322042&di=a37c150bc138b0ec5e2c1b749ca0e868&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb151f8198618367abfffecd72c738bd4b31ce542.jpg","live_name":"高龙龙直播"},"uid":1157677280460801}
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
         * live_type : 1
         * pic : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489070322042&di=a37c150bc138b0ec5e2c1b749ca0e868&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb151f8198618367abfffecd72c738bd4b31ce542.jpg
         * live_name : 高龙龙直播
         */

        private DataBean data;
        private long uid;

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

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public static class DataBean {
            private int live_type;
            private String pic;
            private String live_name;

            public int getLive_type() {
                return live_type;
            }

            public void setLive_type(int live_type) {
                this.live_type = live_type;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getLive_name() {
                return live_name;
            }

            public void setLive_name(String live_name) {
                this.live_name = live_name;
            }
        }
    }
}
