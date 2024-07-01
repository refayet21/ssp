import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CityCorpPoura from './city-corp-poura';
import CityCorpPouraDetail from './city-corp-poura-detail';
import CityCorpPouraUpdate from './city-corp-poura-update';
import CityCorpPouraDeleteDialog from './city-corp-poura-delete-dialog';

const CityCorpPouraRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CityCorpPoura />} />
    <Route path="new" element={<CityCorpPouraUpdate />} />
    <Route path=":id">
      <Route index element={<CityCorpPouraDetail />} />
      <Route path="edit" element={<CityCorpPouraUpdate />} />
      <Route path="delete" element={<CityCorpPouraDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CityCorpPouraRoutes;
