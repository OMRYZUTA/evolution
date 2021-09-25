import * as Utils from "./Utils";
import {string} from "prop-types";

export async function getDetails(timetableID) {
    const url =`/server_Web_exploded/api/timetable/details?timetableid=${timetableID}`;
    const method = 'GET';
    const result = await Utils.fetchWrapper(method, url);
    return result ;
}
