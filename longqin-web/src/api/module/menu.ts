import Http from '../http';

export const menuList = function() {
    return Http.get('/menu/getMenuTree')
}

export const addMenu = function(model: any) {
    return Http.post('/menu/create', model) 
}

export const updateMenu = function(model: any) {
    return Http.post('/menu/update', model) 
}

export const deleteMenu = function(menuId : any) {
    return Http.post('/menu/delete', menuId, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    }) 
}