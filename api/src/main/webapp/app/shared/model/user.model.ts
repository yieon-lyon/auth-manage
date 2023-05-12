export interface IUser {
  email?: string;
  activated?: boolean;
  createdDt?: Date;
  lstLoginDt?: Date;
  pwChangedDt?: Date;
  assignedRoles?: any[];
  assignedClients?: any[];
  assignedUserActivations?: any[];
  disabled?: boolean;
  password?: string;
}

export const defaultValue: Readonly<IUser> = {
  email: '',
  activated: true,
  createdDt: null,
  lstLoginDt: null,
  pwChangedDt: null,
  assignedRoles: [],
  assignedClients: [],
  assignedUserActivations: [],
  disabled: true,
  password: ''
};
