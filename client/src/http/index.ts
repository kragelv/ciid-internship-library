import axios from 'axios';

import { HTTP } from '../config';

const $api = axios.create({
  baseURL: HTTP.API_URL,
});

export default $api;
