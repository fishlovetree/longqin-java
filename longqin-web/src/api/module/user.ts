import Http from '../http';

export const login = function(loginForm: any) {
    return Http.post('/login', loginForm)
}

export const logout = function() {
    return Http.post('/logout')
}

export const userPage = function(queryParams : any) {
    return Http.get('/user/getUserPage', queryParams) 
}

export const addUser = function(model: any) {
    return Http.post('/user/create', model)  
}

export const updateUser = function(model: any) {
    return Http.post('/user/update', model) 
}

export const deleteUser = function(userId: any) {
    return Http.post('/user/delete', userId, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    }) 
}

export const updatePassword = function(params: any) {
    return Http.post('/user/updatePassword', params, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    }) 
}

export const getUserRole = function(userId: any) {
    return Http.get('/user/getUserRole', userId)
}

export const updateUserRole = function(params: any) {
    return Http.post('/user/updateUserRole', params, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    }) 
}