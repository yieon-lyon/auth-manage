import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Badge } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { languages } from 'app/config/translation';
import { getUser } from './user-management.reducer';
import { IRootState } from 'app/shared/reducers';

export interface IUserManagementDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ login: string }> {}

export const UserManagementDetail = (props: IUserManagementDetailProps) => {
  useEffect(() => {
    props.getUser(props.match.params.login);
  }, []);

  const { user } = props;

  const clientInfoEvent = (user, client) => {
    if (user.assignedUserActivations[client]) {
      return <Badge color="info">{client}</Badge>
    } else {
      return <Badge color="danger">{client}</Badge>
    }
  };

  return (
    <div>
      <h2>
        <Translate contentKey="userManagement.detail.title">User</Translate> [<b>{user.email}</b>]
      </h2>
      <Row size="md">
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="userManagement.login">Login</Translate>
          </dt>
          <dd>
            <span>{user.email}</span>&nbsp;
            {user.activated ? (
              <Badge color="success">
                <Translate contentKey="userManagement.activated">Activated</Translate>
              </Badge>
            ) : (
              <Badge color="danger">
                <Translate contentKey="userManagement.deactivated">Deactivated</Translate>
              </Badge>
            )}
          </dd>
          <dt>
            <Translate contentKey="userManagement.createdDt">Created Date</Translate>
          </dt>
          <dd>
            <TextFormat value={user.createdDt !== null ? user.createdDt : '-'} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
          </dd>
          <dt>
            <Translate contentKey="userManagement.lstLoginDt">Last Login Date</Translate>
          </dt>
          <dd>
            <TextFormat value={user.lstLoginDt !== null ? user.lstLoginDt : '-'} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
          </dd>
          <dt>
            <Translate contentKey="userManagement.pwChangedDt">Last Password Changed Date</Translate>
          </dt>
          <dd>
            <TextFormat value={user.pwChangedDt !== null ? user.pwChangedDt : '-'} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
          </dd>
          <dt>
            <Translate contentKey="userManagement.roles">roles</Translate>
          </dt>
          <dd>
            <ul className="list-unstyled">
              {user.assignedRoles
                ? user.assignedRoles.map((role, i) => (
                    <li key={`user-auth-${i}`}>
                      <Badge color="info">{role}</Badge>
                    </li>
                  ))
                : '-'}
            </ul>
          </dd>
          <dt>
            <Translate contentKey="userManagement.clients">clients</Translate>
          </dt>
          <dd>
            <ul className="list-unstyled">
              {user.assignedClients
                ? user.assignedClients.map((client, i) => (
                  <li key={`user-auth-${i}`}>
                    {clientInfoEvent(user, client)}
                  </li>
                ))
                : '-'}
            </ul>
          </dd>
        </dl>
      </Row>
      <Button tag={Link} to="/admin/user-management" replace color="info">
        <FontAwesomeIcon icon="arrow-left" />{' '}
        <span className="d-none d-md-inline">
          <Translate contentKey="entity.action.back">Back</Translate>
        </span>
      </Button>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  user: storeState.userManagement.user
});

const mapDispatchToProps = { getUser };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserManagementDetail);
