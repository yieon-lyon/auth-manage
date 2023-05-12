import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Label, Row, Col } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvFeedback } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { locales, languages } from 'app/config/translation';
import { getUser, getRoles, getClients, updateUser, createUser, reset } from './user-management.reducer';
import { IRootState } from 'app/shared/reducers';

export interface IUserManagementUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ login: string }> {}

export const UserManagementUpdate = (props: IUserManagementUpdateProps) => {
  useEffect(() => {
    props.getUser(props.match.params.login);
    props.getRoles();
    props.getClients();
  }, []);

  const handleClose = () => {
    props.history.push('/admin/user-management');
  };

  const saveUser = (event, values) => {
    props.updateUser(values);
    handleClose();
  };

  const isInvalid = false;
  const { user, loading, updating, roles, clients } = props;

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
            <Translate contentKey="userManagement.home.createOrEditLabel">Create or edit a User</Translate>
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm onValidSubmit={saveUser}>
              {user.email ? (
                <AvGroup>
                  <Label for="id">
                    <Translate contentKey="global.field.email">ID</Translate>
                  </Label>
                  <AvField type="text" className="form-control" name="email" required readOnly value={user.email} />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label for="roles">
                  <Translate contentKey="userManagement.roles">roles</Translate>
                </Label>
                <AvInput type="select" className="form-control" name="assignedRoles" value={user.assignedRoles} multiple>
                  {roles.map(role => (
                    <option value={role} key={role}>
                      {role}
                    </option>
                  ))}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="clients">
                  <Translate contentKey="userManagement.clients">clients</Translate>
                </Label>
                <AvInput type="select" className="form-control" name="assignedClients"  value={user.assignedClients} multiple>
                  {clients.map(client => (
                    <option value={client} key={client}>
                      {client}
                    </option>
                  ))}
                </AvInput>
              </AvGroup>
              <Button tag={Link} to="/admin/user-management" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" type="submit" disabled={isInvalid || updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  user: storeState.userManagement.user,
  roles: storeState.userManagement.roles,
  clients: storeState.userManagement.clients,
  loading: storeState.userManagement.loading,
  updating: storeState.userManagement.updating
});

const mapDispatchToProps = { getUser, getRoles, getClients, updateUser, createUser, reset };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserManagementUpdate);
