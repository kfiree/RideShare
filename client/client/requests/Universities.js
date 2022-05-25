import requests from "./request";
import axios from "axios";
import moment from "moment";
// import token from './../logicApp';


const universities = {

    getAllUniversities: async () => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
            const res = await axios.get('http://localhost:5002/api/universities', config);
            return res.data
        } catch (err) {
            return { errors: err }
        }


        //     const requestOptions = {
        //         method: 'GET',
        //         headers: {
        //             'Content-Type': 'application/json'
        //         }
        //     };
        //     console.log('path', requests.Universities.get);
        //     const result = await fetch('http://localhost:5002/api/universities', requestOptions)
        //         .then(async response => {
        //             const isJson = response.headers.get('content-type')?.includes('application/json');
        //             console.log(1);
        //             console.log('response', response);

        //             const data = isJson && await response.json();
        //             console.log(2);

        //             // check for error response
        //             if (!response.ok) {
        //                 console.log(3);

        //                 // get error message from body or default to response status
        //                 const error = (data && data.errors) || response.status;
        //                 return Promise.reject(error);
        //             }
        //             console.log(4);

        //             return data;
        //             // console.log("data", data)
        //         })
        //         .catch(errors => {
        //             // console.error(errors);
        //             return { errors }
        //         });
        //     return result;
    },
};
export default universities;
