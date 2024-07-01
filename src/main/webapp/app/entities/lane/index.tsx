import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Lane from './lane';
import LaneDetail from './lane-detail';
import LaneUpdate from './lane-update';
import LaneDeleteDialog from './lane-delete-dialog';

const LaneRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Lane />} />
    <Route path="new" element={<LaneUpdate />} />
    <Route path=":id">
      <Route index element={<LaneDetail />} />
      <Route path="edit" element={<LaneUpdate />} />
      <Route path="delete" element={<LaneDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LaneRoutes;
