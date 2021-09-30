import * as Utils from "./Utils";

export async function login(username) {
    const userObject = {"username":username};
    const url ="/server_Web_exploded/api/login";
    const method = 'POST';
    let result;
    try {
         result = await Utils.fetchWrapper(method, url, userObject);
    }catch (e){
        console.log(e)
    }
    return result ;
}
