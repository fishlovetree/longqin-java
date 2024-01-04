import Http from '../http';

export const noticePage = function(queryParams : any) {
    return Http.get('/notice/getNoticePage', queryParams) 
}


export const addNotice = function(model: any) {
    return Http.post('/notice/create', model) 
}

export const updateNotice = function(model: any) {
    return Http.post('/notice/update', model) 
}

export const deleteNotice = function(noticeId : any) {
    return Http.post('/notice/delete', noticeId, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    }) 
}