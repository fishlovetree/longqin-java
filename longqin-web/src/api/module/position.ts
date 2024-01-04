import Http from '../http';

export const positionTree = function() {
    return Http.get('/position/getPositionTree')
}

export const addPosition = function(model: any) {
    return Http.post('/position/create', model) 
}

export const updatePosition = function(model: any) {
    return Http.post('/position/update', model) 
}

export const deletePosition = function(positionId : any) {
    return Http.post('/position/delete', positionId, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    }) 
}