import Http from '../http';

export const logPage = function(queryParams : any) {
    return Http.get('/log/getLogPage', queryParams)
}
