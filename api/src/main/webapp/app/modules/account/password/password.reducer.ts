import axios from 'axios';
import { translate } from 'react-jhipster';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  UPDATE_PASSWORD: 'account/UPDATE_PASSWORD'
};

const initialState = {
  loading: false,
  errorMessage: null,
  updateSuccess: false,
  updateFailure: false
};

export type PasswordState = Readonly<typeof initialState>;

// Reducer
export default (state: PasswordState = initialState, action): PasswordState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.UPDATE_PASSWORD):
      return {
        ...initialState,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case FAILURE(ACTION_TYPES.UPDATE_PASSWORD):
      return {
        ...initialState,
        loading: false,
        updateSuccess: false,
        updateFailure: true
      };
    case SUCCESS(ACTION_TYPES.UPDATE_PASSWORD):
      return {
        ...initialState,
        loading: false,
        updateSuccess: true,
        updateFailure: false
      };
    default:
      return state;
  }
};

// Actions
const apiUrl = 'api';

export const savePassword = (email, currentPassword, newPassword) => ({
  type: ACTION_TYPES.UPDATE_PASSWORD,
  payload: axios.put(`${apiUrl}/change-password`, { email, currentPassword, newPassword }),
  meta: {
    successMessage: translate('password.messages.success'),
    errorMessage: translate('password.messages.error')
  }
});
