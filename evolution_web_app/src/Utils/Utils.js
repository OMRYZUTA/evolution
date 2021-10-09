export function convertObjectToArray(object) {
    return Object.keys(object).map(key => object[key]);
}