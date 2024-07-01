import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pass from './pass';
import PassDetail from './pass-detail';
import PassUpdate from './pass-update';
import PassDeleteDialog from './pass-delete-dialog';

const PassRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Pass />} />
    <Route path="new" element={<PassUpdate />} />
    <Route path=":id">
      <Route index element={<PassDetail />} />
      <Route path="edit" element={<PassUpdate />} />
      <Route path="delete" element={<PassDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PassRoutes;
