import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Gate from './gate';
import GateDetail from './gate-detail';
import GateUpdate from './gate-update';
import GateDeleteDialog from './gate-delete-dialog';

const GateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Gate />} />
    <Route path="new" element={<GateUpdate />} />
    <Route path=":id">
      <Route index element={<GateDetail />} />
      <Route path="edit" element={<GateUpdate />} />
      <Route path="delete" element={<GateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GateRoutes;
