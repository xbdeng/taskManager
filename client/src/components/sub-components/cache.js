import axios from "axios";

var cacheList = []
var cache_index = 0

export function storeRequest(uri, json){
    window.sessionStorage.setItem(cache_index.toString(), JSON.stringify({'uri': uri, 'json':json}))
    cacheList.push(cache_index)
    cache_index = cache_index + 1
}

export function removeRequest(){
    console.log(cacheList)
    while (cacheList.length !== 0){
        console.log('cache out')
        let tmp = cacheList.pop()
        let data = window.sessionStorage.getItem(tmp.toString())
        let st = JSON.parse(data)
        let uri = st.uri
        let json = st.json
        const that = this
        axios.post(
            uri,
            json,
            {
                headers: {
                    Authorization: window.sessionStorage.getItem('token')
                }
            }
        ).then(
            function (response){
                let newToken = response.headers.authorization
                if (newToken != null) window.sessionStorage.setItem('token', newToken)
                window.sessionStorage.removeItem(tmp.toString())
            },
            function (err){
                that.$message({
                    message: 'server error',
                    type: 'error'
                })
            }
        )
    }
}
