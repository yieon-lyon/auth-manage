import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';

import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';
import { defaultValue, IUser } from 'app/shared/model/user.model';

export const ACTION_TYPES = {
  FETCH_ROLES: 'userManagement/FETCH_ROLES',
  FETCH_CLIENTS: 'userManagement/FETCH_CLIENTS',
  FETCH_USERS: 'userManagement/FETCH_USERS',
  FETCH_USER: 'userManagement/FETCH_USER',
  CREATE_USER: 'userManagement/CREATE_USER',
  UPDATE_USER: 'userManagement/UPDATE_USER',
  ACTIVATED: 'userManagement/ACTIVATED',
  DISABLED: 'userManagement/DISABLED',
  REPAIRED: 'userManagement/REPAIRED',
  DELETE_USER: 'userManagement/DELETE_USER',
  RESET: 'userManagement/RESET',
  UPDATE_CLIENT_ACTIVATION: 'userManagement/UPDATE_CLIENT_ACTIVATION'
};

const initialState = {
  loading: false,
  errorMessage: null,
  users: [] as ReadonlyArray<IUser>,
  roles: [] as any[],
  clients: [] as any[],
  user: defaultValue,
  updating: false,
  updateSuccess: false,
  totalItems: 0
};

export type UserManagementState = Readonly<typeof initialState>;

// Reducer
export default (state: UserManagementState = initialState, action): UserManagementState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ROLES):
    case REQUEST(ACTION_TYPES.FETCH_CLIENTS):
      return {
        ...state
      };
    case REQUEST(ACTION_TYPES.FETCH_USERS):
    case REQUEST(ACTION_TYPES.FETCH_USER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USER):
    case REQUEST(ACTION_TYPES.UPDATE_USER):
    case REQUEST(ACTION_TYPES.DELETE_USER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_USERS):
    case FAILURE(ACTION_TYPES.FETCH_USER):
    case FAILURE(ACTION_TYPES.FETCH_ROLES):
    case FAILURE(ACTION_TYPES.CREATE_USER):
    case FAILURE(ACTION_TYPES.UPDATE_USER):
    case FAILURE(ACTION_TYPES.DELETE_USER):
    case FAILURE(ACTION_TYPES.FETCH_CLIENTS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROLES):
      return {
        ...state,
        roles: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CLIENTS):
      return {
        ...state,
        clients: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERS):
      return {
        ...state,
        loading: false,
        users: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_USER):
      return {
        ...state,
        loading: false,
        user: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USER):
    case SUCCESS(ACTION_TYPES.UPDATE_USER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        user: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        user: defaultValue
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    case REQUEST(ACTION_TYPES.DISABLED):
    case REQUEST(ACTION_TYPES.ACTIVATED):
    case REQUEST(ACTION_TYPES.REPAIRED):
    case REQUEST(ACTION_TYPES.UPDATE_CLIENT_ACTIVATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.DISABLED):
    case FAILURE(ACTION_TYPES.ACTIVATED):
    case FAILURE(ACTION_TYPES.REPAIRED):
    case FAILURE(ACTION_TYPES.UPDATE_CLIENT_ACTIVATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.DISABLED):
    case SUCCESS(ACTION_TYPES.ACTIVATED):
    case SUCCESS(ACTION_TYPES.REPAIRED):
    case SUCCESS(ACTION_TYPES.UPDATE_CLIENT_ACTIVATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        user: action.payload.data
      };
    default:
      return state;
  }
};

const apiUrl = 'user';
// Actions
export const getUsers: ICrudGetAllAction<IUser> = (page, size, sort: any) => {
  const requestUrl = `${apiUrl}/list`;
  const body = {
    //
    page: Number(page),
    size: Number(size),
    sort: sort
  };
  return {
    type: ACTION_TYPES.FETCH_USERS,
    payload: axios.post<IUser>(requestUrl, body, {
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
        Accept: 'application/json'
      }
    })
  };
};

export const getRoles = () => ({
  type: ACTION_TYPES.FETCH_ROLES,
  payload: axios.get(`${apiUrl}/roles`)
});

export const getClients = () => ({
  type: ACTION_TYPES.FETCH_CLIENTS,
  payload: axios.get(`${apiUrl}/clients`)
});

export const getUser: ICrudGetAction<IUser> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USER,
    payload: axios.get<IUser>(requestUrl)
  };
};

export const createUser: ICrudPutAction<IUser> = user => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USER,
    payload: axios.post(apiUrl, user)
  });
  dispatch(getUsers());
  return result;
};

export const updateUser: ICrudPutAction<IUser> = user => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USER,
    payload: axios.put(`${apiUrl}/update`, user)
  });
  return result;
};

export const deleteUser: ICrudDeleteAction<IUser> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getUsers());
  return result;
};

export const activatedUser = (email, activated, pageable) => dispatch => {
  return dispatch({
    type: ACTION_TYPES.ACTIVATED,
    payload: axios.put(`${apiUrl}/${email}/activated`, activated, {
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
        Accept: 'application/json'
      }
    })
  }).then(() => {
    dispatch(getUsers(pageable.page, pageable.size, pageable.sort));
  });
};

export const disabledUser = (email, pageable) => dispatch => {
  return dispatch({
    type: ACTION_TYPES.DISABLED,
    payload: axios.delete(`${apiUrl}/${email}/delete`, {
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
        Accept: 'application/json'
      }
    })
  }).then(() => {
    dispatch(getUsers(pageable.page, pageable.size, pageable.sort));
  });
};

export const repairUser = (email, pageable) => dispatch => {
  return dispatch({
    type: ACTION_TYPES.REPAIRED,
    payload: axios.get(`${apiUrl}/${email}/repair`, {
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
        Accept: 'application/json'
      }
    })
  }).then(() => {
    dispatch(getUsers(pageable.page, pageable.size, pageable.sort));
  });
};

export const updateClientActivation = (email, activationForm, pageable) => dispatch => {
  return dispatch({
    type: ACTION_TYPES.UPDATE_CLIENT_ACTIVATION,
    payload: axios.put(`${apiUrl}/${email}/client-activated`, activationForm, {
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
        Accept: 'application/json'
      }
    })
  }).then(() => {
    dispatch(getUsers(pageable.page, pageable.size, pageable.sort));
  });
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
