import * as Utils from "./Utils";

export async function getDetails(timetableID) {
    const url = `/server_Web_exploded/api/timetable/details?timetableID=${timetableID}`;
    const method = 'GET';
    const result = await Utils.fetchWrapper(method, url);
    return result;
}
