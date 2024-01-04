import Http from '../http';

export const organizationList = function() {
    return Http.get('/organization/getOrganizationList')
}

export const organizationPage = function(queryParams : any) {
    return Http.get('/organization/getOrganizationPage', queryParams)
}

export const addOrganization = function(model: any) {
    return Http.post('/organization/create', model)  
}

export const updateOrganization = function(model: any) {
    return Http.post('/organization/update', model) 
}

export const deleteOrganization = function(organizationId : any) {
    return Http.post('/organization/delete', organizationId, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    }) 
}