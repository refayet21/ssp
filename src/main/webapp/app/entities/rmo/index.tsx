import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RMO from './rmo';
import RMODetail from './rmo-detail';
import RMOUpdate from './rmo-update';
import RMODeleteDialog from './rmo-delete-dialog';

const RMORoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RMO />} />
    <Route path="new" element={<RMOUpdate />} />
    <Route path=":id">
      <Route index element={<RMODetail />} />
      <Route path="edit" element={<RMOUpdate />} />
      <Route path="delete" element={<RMODeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RMORoutes;
