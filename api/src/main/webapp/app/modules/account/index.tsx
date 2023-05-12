import React from 'react';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Password from './password/password';

const Routes = ({ match }) => (
  <div>
    <ErrorBoundaryRoute path={`${match.url}/password`} component={Password} />
  </div>
);

export default Routes;
