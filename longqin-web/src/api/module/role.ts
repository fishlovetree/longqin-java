import Http from '../http';

export const roleList = function() {
    return Http.get('/role/getRoleList')
}

export const addRole = function(model: any) {
    return Http.post('/role/create', model)  
}

export const updateRole = function(model: any) {
    return Http.post('/role/update', model) 
}

export const deleteRole = function(roleId: any) {
    return Http.post('/role/delete', roleId, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    }) 
}

export const getRoleMenu = function(roleId: any) {
    return Http.get('/role/getRoleMenu', roleId)
}

export const updateRoleMenu = function(params: any) {
    return Http.post('/role/updateRoleMenu', params, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    }) 
}