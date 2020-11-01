import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILoanPrincipal, defaultValue } from 'app/shared/model/loan-principal.model';

export const ACTION_TYPES = {
  FETCH_LOANPRINCIPAL_LIST: 'loanPrincipal/FETCH_LOANPRINCIPAL_LIST',
  FETCH_LOANPRINCIPAL: 'loanPrincipal/FETCH_LOANPRINCIPAL',
  CREATE_LOANPRINCIPAL: 'loanPrincipal/CREATE_LOANPRINCIPAL',
  UPDATE_LOANPRINCIPAL: 'loanPrincipal/UPDATE_LOANPRINCIPAL',
  DELETE_LOANPRINCIPAL: 'loanPrincipal/DELETE_LOANPRINCIPAL',
  RESET: 'loanPrincipal/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILoanPrincipal>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type LoanPrincipalState = Readonly<typeof initialState>;

// Reducer

export default (state: LoanPrincipalState = initialState, action): LoanPrincipalState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LOANPRINCIPAL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LOANPRINCIPAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_LOANPRINCIPAL):
    case REQUEST(ACTION_TYPES.UPDATE_LOANPRINCIPAL):
    case REQUEST(ACTION_TYPES.DELETE_LOANPRINCIPAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_LOANPRINCIPAL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LOANPRINCIPAL):
    case FAILURE(ACTION_TYPES.CREATE_LOANPRINCIPAL):
    case FAILURE(ACTION_TYPES.UPDATE_LOANPRINCIPAL):
    case FAILURE(ACTION_TYPES.DELETE_LOANPRINCIPAL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOANPRINCIPAL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOANPRINCIPAL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_LOANPRINCIPAL):
    case SUCCESS(ACTION_TYPES.UPDATE_LOANPRINCIPAL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_LOANPRINCIPAL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/loan-principals';

// Actions

export const getEntities: ICrudGetAllAction<ILoanPrincipal> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LOANPRINCIPAL_LIST,
    payload: axios.get<ILoanPrincipal>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ILoanPrincipal> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LOANPRINCIPAL,
    payload: axios.get<ILoanPrincipal>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ILoanPrincipal> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOANPRINCIPAL,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILoanPrincipal> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LOANPRINCIPAL,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILoanPrincipal> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOANPRINCIPAL,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
