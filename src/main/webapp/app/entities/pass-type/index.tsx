import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PassType from './pass-type';
import PassTypeDetail from './pass-type-detail';
import PassTypeUpdate from './pass-type-update';
import PassTypeDeleteDialog from './pass-type-delete-dialog';

const PassTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PassType />} />
    <Route path="new" element={<PassTypeUpdate />} />
    <Route path=":id">
      <Route index element={<PassTypeDetail />} />
      <Route path="edit" element={<PassTypeUpdate />} />
      <Route path="delete" element={<PassTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PassTypeRoutes;
