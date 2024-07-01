import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleAssignment from './vehicle-assignment';
import VehicleAssignmentDetail from './vehicle-assignment-detail';
import VehicleAssignmentUpdate from './vehicle-assignment-update';
import VehicleAssignmentDeleteDialog from './vehicle-assignment-delete-dialog';

const VehicleAssignmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleAssignment />} />
    <Route path="new" element={<VehicleAssignmentUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleAssignmentDetail />} />
      <Route path="edit" element={<VehicleAssignmentUpdate />} />
      <Route path="delete" element={<VehicleAssignmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleAssignmentRoutes;
