import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HorseComponent} from './component/horse/horse.component';
import {OwnerComponent} from './component/owner/owner.component';
import {HorseDetailComponent} from './component/horse/horse-detail/horse-detail.component';
import {HorseCreateEditComponent, HorseCreateEditMode} from './component/horse/horse-create-edit/horse-create-edit.component';
import {OwnerCreateComponent} from './component/owner/owner-create/owner-create.component';

const routes: Routes = [
  {path: '', redirectTo: 'horses', pathMatch: 'full'},
  {path: 'horses', children: [
    {path: '', component: HorseComponent},
    {path: 'create', component: HorseCreateEditComponent, data: {mode: HorseCreateEditMode.create}},
    {path: ':id', component: HorseDetailComponent},
    {path: ':id/edit', component: HorseCreateEditComponent, data: {mode: HorseCreateEditMode.edit}},
  ]},
 // {path: '**', redirectTo: 'horses'},
  {path: 'owners', children: [
      {path: '', component: OwnerComponent},
      {path: 'create', component: OwnerCreateComponent},
    ]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
