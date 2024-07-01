import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleType from './vehicle-type';
import VehicleTypeDetail from './vehicle-type-detail';
import VehicleTypeUpdate from './vehicle-type-update';
import VehicleTypeDeleteDialog from './vehicle-type-delete-dialog';

const VehicleTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleType />} />
    <Route path="new" element={<VehicleTypeUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleTypeDetail />} />
      <Route path="edit" element={<VehicleTypeUpdate />} />
      <Route path="delete" element={<VehicleTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleTypeRoutes;
