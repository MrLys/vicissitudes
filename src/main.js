// This is the main.js file. Import global CSS and scripts here.
// The Client API can be used here. Learn more: gridsome.org/docs/client-api

import DefaultLayout from '~/layouts/Default.vue';
import AxiosPlugin from '~/axios.js';
import Vuex from 'vuex';

export default function (Vue, { router, head, isClient, appOptions}) {
    // Set default layout as a global component
    Vue.component('Layout', DefaultLayout);
    Vue.use(AxiosPlugin);
    Vue.use(Vuex);
    const token = localStorage.getItem('token')
    if (token) {
      Vue.prototype.$http.defaults.headers.common['Authorization'] = 'Bearer ' + token
      console.log(Vue.prototype.$http.defaults.headers.common['Authorization']);
    }
    appOptions.store = new Vuex.Store({
        state: {
            status: '',
            token:  localStorage.getItem('token') || '',
            id: localStorage.getItem('user_id') || ''
        },
        mutations: {
            auth_request(state){
                state.status = 'loading'
            },
            auth_success(state, payload){
                state.status = 'success'
                state.token = payload.token
                state.id = payload.user_id
            },
            auth_error(state){
                state.status = 'error'
            },
            register_success(state) {
              state.status = "success";
            },
            logout(state){
                state.status = ''
                state.token = ''
            },
        },
        actions: {
          login({commit}, data){
            return new Promise((resolve, reject) => {
              commit('auth_request')
              let options = {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                  'Authorization': "Basic " + data,
                }}
              Vue.prototype.$http('/api/login', options)
                .then(resp => {
                  const token = resp.data.token
                  const user_id = resp.data.id
                  console.log(user_id);
                  localStorage.setItem('token', token)
                  localStorage.setItem('user_id', user_id)
                  Vue.prototype.$http.defaults.headers.common['Authorization'] = 'Bearer ' + token
                  commit('auth_success', { token: token, user_id: user_id})
                  resolve(resp)
                })
                .catch(err => {
                  commit('auth_error')
                  localStorage.removeItem('token')
                  reject(err)
                })
            })
          },
          logout({commit}){
            return new Promise((resolve, reject) => {
              commit('logout')
              localStorage.removeItem('token')
              localStorage.removeItem('user_id')
              delete Vue.prototype.$http.defaults.headers.common['Authorization']
              resolve()
            })

          },
          register({commit}, data) {
            return new Promise((resolve, reject) => {
              commit('auth_request')
              Vue.prototype.$http.post('/api/register', data)
                .then(resp => {
                  commit('register_success');
                  resolve(resp);
                })
                .catch(err => {
                  commit('auth_error')
                  localStorage.removeItem('token')
                  reject(err)
                })
            })
          },


        },
      getters : {
        isLoggedIn: state => !!state.token,
        authStatus: state => state.status,
        id: state => state.id,
      }

    })
}
