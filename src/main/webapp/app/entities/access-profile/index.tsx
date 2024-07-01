import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AccessProfile from './access-profile';
import AccessProfileDetail from './access-profile-detail';
import AccessProfileUpdate from './access-profile-update';
import AccessProfileDeleteDialog from './access-profile-delete-dialog';

const AccessProfileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AccessProfile />} />
    <Route path="new" element={<AccessProfileUpdate />} />
    <Route path=":id">
      <Route index element={<AccessProfileDetail />} />
      <Route path="edit" element={<AccessProfileUpdate />} />
      <Route path="delete" element={<AccessProfileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AccessProfileRoutes;
