import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Table, Button, Input, Row, Badge } from 'reactstrap';
import { Translate, TextFormat, JhiPagination, JhiItemCount, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { getUsers, updateUser, activatedUser, disabledUser, repairUser, updateClientActivation } from './user-management.reducer';
import { IRootState } from 'app/shared/reducers';

export interface IUserManagementProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}

export const UserManagement = (props: IUserManagementProps) => {
  const [pagination, setPagination] = useState(getSortState(props.location, ITEMS_PER_PAGE));
  const [filter, setFilter] = useState('');

  const envFilterFn = userProps => userProps.email.toUpperCase().includes(filter.toUpperCase());

  const paging = {
    page: pagination.activePage - 1,
    size: pagination.itemsPerPage,
    sort:`${pagination.order}`
  };

  const changeFilter = evt => setFilter(evt.target.value);

  useEffect(() => {
    props.getUsers(pagination.activePage - 1, pagination.itemsPerPage, `${pagination.order}`);
    // props.history.push(`${props.location.pathname}`);
  }, [pagination]);

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage
    });

  const toggleActive = user => () =>
    props.activatedUser(user.email, !user.activated, paging);

  const disableUser = user => () =>
    props.disabledUser(user.email, paging);

  const repairUser = user => () =>
    props.repairUser(user.email, paging);

  const clientInfoEvent = (user, client) => {
    if (user.assignedUserActivations[client]) {
      return <Badge className="hand" onClick={() => clientUpdate(user, client, false)} color="info">{client}</Badge>
    } else {
      return <Badge className="hand" onClick={() => clientUpdate(user, client, true)} color="danger">{client}</Badge>
    }
  };

  const clientUpdate = (user, client, value) => {
    const activationForm = {
      clientInfo: client,
      activated: value
    };
    props.updateClientActivation(user.email, activationForm, paging);
  };

  const { users, account, match, totalItems } = props;
  return (
    <div>
      <h2 id="user-management-page-heading">
        <Translate contentKey="userManagement.home.title">Users</Translate>
        {/*<Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity">*/}
        {/*  <FontAwesomeIcon icon="plus" /> <Translate contentKey="userManagement.home.createLabel">Create a new user</Translate>*/}
        {/*</Link>*/}
      </h2>
      <Input type="search" value={filter} onChange={changeFilter} name="search" id="search" placeholder="Email filter"/>
      <Table responsive striped>
        <thead>
          <tr>
            <th>
              <Translate contentKey="userManagement.email">Email</Translate>
            </th>
            <th>
              <Translate contentKey="userManagement.activate">Activate</Translate>
            </th>
            <th>
              <Translate contentKey="userManagement.roles">Role Group</Translate>
            </th>
            <th>
              <Translate contentKey="userManagement.clients">Client Info Group</Translate>
            </th>
            <th>
              <Translate contentKey="userManagement.createdDt">Created Date</Translate>
            </th>
            <th>
              <Translate contentKey="userManagement.lstLoginDt">Last Login Date</Translate>
            </th>
            <th>
              <Translate contentKey="userManagement.pwChangedDt">Last Password Changed Date</Translate>
            </th>
            <th>
              <Translate contentKey="userManagement.disabled">Disable</Translate>
            </th>
            <th />
          </tr>
        </thead>
        <tbody>
          {users.filter(envFilterFn).map((user, i) => (
            <tr id={user.email} key={`user-${i}`}>
              <td>{user.email}</td>
              <td>
                {user.activated ? (
                  <Button color="success" onClick={toggleActive(user)}>
                    <Translate contentKey="userManagement.activated">Activated</Translate>
                  </Button>
                ) : (
                  <Button color="danger" onClick={toggleActive(user)}>
                    <Translate contentKey="userManagement.deactivated">Deactivated</Translate>
                  </Button>
                )}
              </td>
              <td>
                {user.assignedRoles
                  ? user.assignedRoles.map((role, j) => (
                      <div key={`user-auth-${i}-${j}`}>
                        <Badge color="info">{role}</Badge>
                      </div>
                    ))
                  : '-'}
              </td>
              <td>
                {user.assignedClients
                  ? user.assignedClients.map((client, j) => (
                    <div key={`user-auth-${i}-${j}`}>
                      {clientInfoEvent(user, client)}
                    </div>
                  ))
                  : '-'}
              </td>
              <td>
                <TextFormat value={user.createdDt !== null ? user.createdDt : '-'} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
              </td>
              <td>
                <TextFormat value={user.lstLoginDt !== null ? user.lstLoginDt : '-'} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
              </td>
              <td>
                <TextFormat value={user.pwChangedDt !== null ? user.pwChangedDt : '-'} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
              </td>
              <td>
                {user.disabled ? (
                  <Button color="danger" onClick={repairUser(user)}>
                    <Translate contentKey="userManagement.lock">Lock</Translate>
                  </Button>
                ) : (
                  <Button color="success" onClick={disableUser(user)}>
                    <Translate contentKey="userManagement.unlock">UnLock</Translate>
                  </Button>
                )}
              </td>
              <td className="text-right">
                <div className="btn-group flex-btn-group-container">
                  <Button tag={Link} to={`${match.url}/${user.email}`} color="info" size="sm">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.view">View</Translate>
                    </span>
                  </Button>
                  <Button tag={Link} to={`${match.url}/${user.email}/edit`} color="primary" size="sm">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.edit">Edit</Translate>
                    </span>
                  </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <div className={users && users.length > 0 ? '' : 'd-none'}>
        <Row className="justify-content-center">
          <JhiItemCount page={pagination.activePage} total={totalItems} itemsPerPage={pagination.itemsPerPage} i18nEnabled />
        </Row>
        <Row className="justify-content-center">
          <JhiPagination
            activePage={pagination.activePage}
            onSelect={handlePagination}
            maxButtons={5}
            itemsPerPage={pagination.itemsPerPage}
            totalItems={props.totalItems}
          />
        </Row>
      </div>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  totalItems: storeState.userManagement.totalItems,
  account: storeState.authentication.account
});

const mapDispatchToProps = { getUsers, updateUser, activatedUser, disabledUser, repairUser, updateClientActivation };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserManagement);
