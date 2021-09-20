import axios from "axios";


export async function getAll() {
    // try {
    const result = await axios('/date/');
    console.log(result);
    return result;
    // } catch (err) {
    //     StaticServices.handleError(err);
    //     return []; //in case we have a problem getting the applications
    // }
}

export async function addNew(user) {
    const result = await fetch("/login/", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'

        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(user)
    });
    console.log(result);
    return result.data;
}
//
// export async function update(app) {
//     const result = await axios.put(app.url, app);
//     return result.data;
// }
//
// export async function remove(app) {
//     return await axios.delete(app.url);
// }