import axios from 'axios'

const apiConnection = axios.create({
    baseURL: 'http://localhost:8080'
})

export default apiConnection
