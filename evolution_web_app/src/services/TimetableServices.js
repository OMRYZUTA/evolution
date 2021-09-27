import * as Utils from "./Utils";

export async function getDetails(timetableID) {
    const url = `/server_Web_exploded/api/timetable/details?timetableid=${timetableID}`;
    const method = 'GET';

    return result;
    const result = await Utils.fetchWrapper(method, url);
}
