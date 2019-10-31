import axios from 'axios';

const getJobInItClient = axios.create({
  baseURL: "http://localhost:8080" // api gateway address
})

export default getJobInItClient;