new Vue({
    el: "#app",
    data:{
        file:{}
    },
    methods: {
        fileUpload: function () {
            console.info("sbkjauth:" + localStorage.getItem("sbkjauth"));
            var _this = this;
            var formData = new FormData();
            formData.append("uploadFile", $("input[name='uploadFile']")[0].files[0]);
            axios({
                method: "post",
                url: "/auth/fileUploadLocal" + getSign(),
                data: formData,
                headers: {
                    'Content-Type': 'multipart/form-data',  // 文件上传
                    // 'Content-Type': 'application/x-www-form-urlencoded',  // 表单
                    // 'Content-Type': 'application/json;charset=UTF-8'  // json
                    "sbkjauth": localStorage.getItem("sbkjauth")
                },
            }).then(function (response) {
                console.log(response);
                alert(response.data.message);
                _this.file=response.data.result;
                console.info(_this.file);
                console.info(_this.file.url);
            }).catch(function (reason) {

            })
        },

    }
});