new Vue({
    el: '#app',
    data: {
        user: {},
        result: {}
    },
    // 发送post请求时,不能发送 Content-Type: application/json;charset=UTF-8 这个格式,因为后台过滤器要进行处理签名
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        // 'Content-Type': 'application/json;charset=UTF-8'
    },

    methods: {
        login: function () {
            var _this = this;
            axios({
                method: 'post',
                url: '/noauth/login' + getSign("username=" + _this.user.username + "&password=" + _this.user.password),
                data: {
                    username: _this.user.username,
                    password: _this.user.password
                },
                transformRequest: [function (data) {  // 将{username:111,password:111} 转成 username=111&password=111
                    var ret = '';
                    for (var it in data) {
                        // 如果要发送中文 编码
                        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                    }
                    return ret.substring(0, ret.length - 1)
                }],
            }).then(function (response) {
                _this.result = response.data;
                alert(_this.result.message);
                localStorage.setItem("sbkjauth", response.headers["sbkjauth"]);
                if (_this.result.status == "0201") {
                    location.href = "/html/index.html";
                }
            }).then(function (resp) {
                console.info(resp.data);
            }).catch(function (reason) {
                console.error(reason)
            })
        },
        outLogin: function (username, params) {
            var url = "/auth/outLogin";
            var _this = this;
            axios.get(url + params + "&username=" + username).then(function (response) {
                console.log(response);
                _this.result = response.data;
                console.log(_this.result);
            }).catch(function (reason) {

            })
        },
    },
    created:

        function () {  // 页面加载成功
            // this.login();
            console.info("页面尚未加载完成!")
        }

})
;