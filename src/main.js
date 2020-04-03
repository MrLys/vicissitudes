// This is the main.js file. Import global CSS and scripts here.
// The Client API can be used here. Learn more: gridsome.org/docs/client-api

import DefaultLayout from '~/layouts/Default.vue';
import AxiosPlugin from '~/axios.js';
import Vuex from 'vuex';
import moment from 'moment';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

function getItem(key) {
    if (typeof window !== 'undefined' && window) {
        return window.localStorage.getItem(key);
    }
}
function setItem(key, value) {
    if (typeof window !== 'undefined' && window) {
        window.localStorage.setItem(key, value);
    }

}
function removeItem(key) {
    if (typeof window !== 'undefined' && window) {
        return window.localStorage.removeItem(key);
    }
}

export default function (Vue, { router, head, isClient, appOptions}) {
    // Set default layout as a global component
    Vue.component('Layout', DefaultLayout);
    Vue.use(AxiosPlugin);
    Vue.use(Vuex);
    Vue.prototype.$moment = moment;
    const windowGlobal = typeof window !== 'undefined' && window;
    const token = getItem('token')
    const url = process.env.RUTTA_URL;
    if (token) {
      Vue.prototype.$http.defaults.headers.common['Authorization'] = 'Bearer ' + token
      console.log(Vue.prototype.$http.defaults.headers.common['Authorization']);
    }
    NProgress.configure();
    if (windowGlobal) {
        router.beforeEach((to, from, next) => {
            NProgress.start()
            next()
        })

        router.afterEach((to, from) => {
            NProgress.done()
        })
    }
    appOptions.store = new Vuex.Store({
        state: {
            status: '',
            token:  getItem('token') || '',
            id: getItem('user_id') || ''
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
            forgot_password(state) {
              state.token = ''
              state.status = "loading";
            },
          awaiting_recovery_link(state) {
            state.status = "loading";
          },
            register_success(state) {
              state.status = "success";
            },
            logout(state){
                state.status = ''
                state.token = ''
                setItem('token', '');
                setItem('start_date','');
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
              Vue.prototype.$http(url+'/api/login', options)
                .then(resp => {
                  const token = resp.data.token
                  const user_id = resp.data.id
                  console.log(user_id);
                  setItem('token', token)
                  setItem('user_id', user_id)
                  Vue.prototype.$http.defaults.headers.common['Authorization'] = 'Bearer ' + token
                  commit('auth_success', { token: token, user_id: user_id})
                  resolve(resp)
                })
                .catch(err => {
                  commit('auth_error')
                  removeItem('token')
                  reject(err)
                })
            })
          },
          logout({commit}){
            return new Promise((resolve, reject) => {
              commit('logout')
              removeItem('token')
              removeItem('user_id')
              delete Vue.prototype.$http.defaults.headers.common['Authorization']
              resolve()
            })
          },
          forgot_password({commit}, data) {
            return new Promise((resolve, reject) => {
              commit('forgot_password')
              Vue.prototype.$http.post(url+'/api/forgot?email='+data)
                .then(resp => {
                  commit('awaiting_recovery_link');
                  resolve(resp);
                })
                .catch(err => {
                  removeItem('token')
                  reject(err)
                })
            })
          },
          register({commit}, data) {
            return new Promise((resolve, reject) => {
              commit('auth_request')
              Vue.prototype.$http.post(url+'/api/register', data)
                .then(resp => {
                  commit('register_success');
                  resolve(resp);
                })
                .catch(err => {
                  commit('auth_error')
                  removeItem('token')
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
