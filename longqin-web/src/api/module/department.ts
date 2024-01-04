import Http from '../http';

export const departmentTree = function() {
    return Http.get('/department/getDepartmentTree')
}

export const addDepartment = function(model: any) {
    return Http.post('/department/create', model) 
}

export const updateDepartment = function(model: any) {
    return Http.post('/department/update', model) 
}

export const deleteDepartment = function(departmentId : any) {
    return Http.post('/department/delete', departmentId, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    }) 
}