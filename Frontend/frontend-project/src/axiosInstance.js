import axios from 'axios';
import { useNavigate } from 'react-router-dom';


const config = {
    baseURL: 'http://localhost:8080/api', // Osnova URL-a na backendu
    
    timeout: 60000, 
    headers: {
        'Content-Type': 'application/json', 
    },
    
    
};


const _axios = axios.create(config);


_axios.interceptors.request.use(
    (request) => {
        const jwt = localStorage.getItem('jwt');
        if (jwt) {
            request.headers.Authorization = `Bearer ${jwt}`; 
        }
        return request;
    },
    (error) => {
      
        return Promise.reject(error);
    }
);


_axios.interceptors.response.use(
    (response) => {
        
        return response;
    },
    (error) => {
     
        if (error.response && error.response.status === 401) {
            const navigate = useNavigate();
            navigate('/home'); 
        }
        return Promise.reject(error);
    }
);


export default _axios;
