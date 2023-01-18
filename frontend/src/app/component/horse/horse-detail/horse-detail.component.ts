import {Component, OnInit} from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {Observable, of} from 'rxjs';
import {Horse} from 'src/app/dto/horse';
import {Owner} from 'src/app/dto/owner';
import {Sex} from 'src/app/dto/sex';
import {HorseService} from 'src/app/service/horse.service';
import {OwnerService} from 'src/app/service/owner.service';

@Component({
  selector: 'app-horse-detail',
  templateUrl: './horse-detail.component.html',
  styleUrls: ['./horse-detail.component.scss']
})
export class HorseDetailComponent implements OnInit {

  horse: Horse = {
    name: '',
    description: '',
    dateOfBirth: new Date(),
    sex: Sex.female,
  };

  horseMother: string | null = null;
  horseFather: string | null = null;

  bannerError: string | null = null;

  mother: string | null = null;
  father: string | null = null;

  id=1;


  constructor(
    private service: HorseService,
    private ownerService: OwnerService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) {

  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if(id!=null){
    this.id =Number(id);
    }

    this.loadHorse(this.id);
}

  deleteHorse(){
      console.log('Delete horse');
      this.service.deleteHorse(this.id).subscribe(
           (data) => {
               console.log('Successfully deleted horse');
               this.notification.success('Horse successfully deleted. User will be redirected soon.');
                  setTimeout(() => {
                    this.router.navigate(['horses/']);
                  }, 2500);
                 }, error => {
                              console.error('Error deleting', error);
                              const errorMessage = error.status === 0
                                ? 'Is the backend up?'
                                : error.message.message;
                              this.notification.error(errorMessage, 'Could Not Delete the Horse');
                            }
               );
         }

  public moveToHorse(id: number) {
     this.horse= {
       name: '',
       description: '',
       dateOfBirth: new Date(),
       sex: Sex.female,
       mother:undefined,
       father:undefined
     };
     if(this.horse.mother!=null){
     this.horse.mother.name= '';
     }
     if(this.horse.father!=null){
     this.horse.father.name= '';
     }
     this.mother=null;
     this.father=null;
     this.id=id;
     this.horseMother='';
     this.horseFather='';
     this.horse.mother=undefined;
     this.horse.father=undefined;
     this.loadHorse(this.id);
  }

  public formatOwnerName(owner: Owner | null | undefined): string {
    return (owner == null)
      ? ''
      : `${owner.firstName} ${owner.lastName}`;
  }

  public formatMotherName(mother: Horse | null | undefined): string {
    return (mother == null)
      ? ''
      : `${mother.name}`;
  }

  public formatFatherName(father: Horse | null | undefined): string {
    return (father == null)
      ? ''
      : `${father.name}`;
  }

 private loadHorse(id: number) {
    this.service.getHorseById(id).subscribe(
      (horse: Horse) => {
       this.horse = horse;
       if(horse.mother){
       this.mother= horse.mother.name;
       }
       if(horse.father){
              this.father= horse.father.name;
              }
      },
      error => {
       console.error('Error could not fetch horse',  error);
       this.bannerError = 'Could not fetch horses: ' + error.message;
       const errorMessage = error.status === 0
       ? 'Is the backend up?'
       : error.message.message;
       this.notification.error(errorMessage, 'Could Not Fetch Horses');
      }
    );
  }



}
