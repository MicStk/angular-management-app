import {HttpClient, HttpParams} from '@angular/common/http';
import {formatDate} from '@angular/common';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Horse} from '../dto/horse';
import {HorseSearch} from '../dto/horse';
import {Sex} from '../dto/sex';

const baseUri = environment.backendUrl + '/horses';

@Injectable({
  providedIn: 'root'
})
export class HorseService {

  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Get all horses stored in the system
   *
   * @return observable list of found horses.
   */
  getAll(): Observable<Horse[]> {
    console.log('Get all horses');
    return this.http.get<Horse[]>(baseUri + '/all');
  }

  /**
   * Get horse with the given id
   *
   * @param id of the horse
   * @return observable of received horse.
   */
  getHorseById(id: number): Observable<Horse> {
    console.log('Get horse with ' + id);
    return this.http.get<Horse>(baseUri + '/' + id);
  }

 /**
  * Search for horses by name
  *
  * @return observable list of found horses.
  */
  public searchByName(name: string, limitTo: number): Observable<Horse[]> {
     console.log('Search for horses by name');
      const params = new HttpParams()
        .set('name', name)
        .set('limit', limitTo);
      return this.http.get<Horse[]>(baseUri + '/parentsearch', { params });
    }

  /**
   * Search for horses by search
   *
   * @return observable list of found horses.
   */
   public searchHorse(horse: HorseSearch): Observable<Horse[]> {
   console.log('Search Horse' + horse);
   let params = new HttpParams();
   if(horse.name!=null){
          params = params.set('name', horse.name);
   }
   if(horse.description!=null && horse.description!==undefined && horse.description!=='') {
             params = params.set('description', horse.description);
   }
   if(horse.bornBefore!==undefined && horse.bornBefore!=null){

          const birthday = formatDate(horse.bornBefore,'yyyy-MM-dd','en-US');
          params = params.set('bornBefore', birthday);
   }
   if(horse.sex!=null && horse.sex!=='NONE'){
      params = params.set('sex', horse.sex);
   }

   if(horse.owner!=null && horse.owner!==undefined){
          if (horse.owner.id!=null && horse.owner.id!==undefined){
          params = params.set('ownerID', horse.owner.id);
          }
   }

   return this.http.get<Horse[]>(baseUri + '/search', { params });

   }

  /**
   * Create a new horse in the system.
   *
   * @param horse the data for the horse that should be created
   * @return an Observable for the created horse
   */
  create(horse: Horse): Observable<Horse> {
    console.log('Create horse: ' + horse);
    return this.http.post<Horse>(
      baseUri,
      horse
    );
  }

  /**
   * Update a new horse in the system.
   *
   * @param horse the data for the horse that should be updated
   * @return an Observable for the updated horse
   */
  update(horse: Horse, id: number): Observable<Horse> {
    console.log('Update horse: ' + horse);
    return this.http.put<Horse>(
      baseUri + '/' + id,
      horse
    );
  }

  /**
   * Delete a horse with a given id in the system.
   *
   * @param id the id of the horse that should be deleted
   */
  deleteHorse(id: number): Observable<Horse> {
     console.log('Delete horse with ID ', id);
      return this.http.delete<Horse>(baseUri + '/' + id);
  }

}
