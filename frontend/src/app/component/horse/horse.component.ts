import {Component, OnInit} from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable, of} from 'rxjs';
import {HorseService} from 'src/app/service/horse.service';
import {Horse} from '../../dto/horse';
import {HorseSearch} from '../../dto/horse';
import {Owner} from '../../dto/owner';
import {Sex} from 'src/app/dto/sex';
import {OwnerService} from 'src/app/service/owner.service';

@Component({
  selector: 'app-horse',
  templateUrl: './horse.component.html',
  styleUrls: ['./horse.component.scss']
})
export class HorseComponent implements OnInit {
  search = false;
  horses: Horse[] = [];
  bannerError: string | null = null;

  deleteId = 1;

  searchHorse: HorseSearch = {
      name: '',
      description: '',
      bornBefore: undefined,
      sex: Sex.none,
    };

  constructor(
    private service: HorseService,
    private ownerService: OwnerService,
    private notification: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {

  }

  reloadHorses(form: NgForm): void {
  console.log('Search for Horses:');
   this.service.searchHorse(this.searchHorse)
      .subscribe({
        next: data => {
          this.horses = data;
        },
        error: error => {
          console.error('Error fetching horses', error);
          this.bannerError = 'Could not fetch horses: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Horses');
        }
      });
  }


  public formatOwnerName(owner: Owner | null | undefined): string {
      return (owner == null)
        ? ''
        : `${owner.firstName} ${owner.lastName}`;
    }

  ownerName(owner: Owner | null): string {
    return owner
      ? `${owner.firstName} ${owner.lastName}`
      : '';
  }

  ownerSuggestions = (input: string) => (input === '')
     ? of([])
     : this.ownerService.searchByName(input, 5);

  dateOfBirthAsLocaleDate(horse: Horse): string {
     if(horse.dateOfBirth!=null){
        return new Date(horse.dateOfBirth).toLocaleDateString();
      } else {return new Date().toLocaleDateString();}
  }

  delete(id: number){
      this.deleteId=id;
  }

  deleteHorse(){
  console.log('Delete Horse with Id' + this.deleteId);
      this.service.deleteHorse(this.deleteId).subscribe(
           (data) => {
               console.log('Successfully deleted horse');
               this.notification.success('Horse successfully deleted. User will be redirected soon.');
                  setTimeout(() => {
                       this.service.searchHorse(this.searchHorse)
                          .subscribe({
                            next: data2 => {
                              this.horses = data2;
                            },
                            error: error => {
                              console.error('Error fetching horses', error);
                              this.bannerError = 'Could not fetch horses: ' + error.message;
                              const errorMessage = error.status === 0
                                ? 'Is the backend up?'
                                : error.message.message;
                              this.notification.error(errorMessage, 'Could Not Fetch Horses');
                            }
                          });
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

}
