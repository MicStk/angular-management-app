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


export enum HorseCreateEditMode {
  create,
  edit,
};

@Component({
  selector: 'app-horse-create-edit',
  templateUrl: './horse-create-edit.component.html',
  styleUrls: ['./horse-create-edit.component.scss']
})
export class HorseCreateEditComponent implements OnInit {

  mode: HorseCreateEditMode = HorseCreateEditMode.create;
  horse: Horse = {
    name: '',
    description: '',
    dateOfBirth: undefined,
    sex: Sex.female,
  };

    mother: string | null = null;
    father: string | null = null;

   successDelete = false;
   deletedHorse = '';

  id =1;


  constructor(
    private service: HorseService,
    private ownerService: OwnerService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) {
  }

  public get heading(): string {
    switch (this.mode) {
      case HorseCreateEditMode.create:
        return 'Create New Horse';
      case HorseCreateEditMode.edit:
              return 'Edit New Horse';
      default:
        return '?';
    }
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case HorseCreateEditMode.create:
        return 'Create';
      case HorseCreateEditMode.edit:
              return 'Save';
      default:
        return '?';
    }
  }

  get modeIsCreate(): boolean {
    return this.mode === HorseCreateEditMode.create;
  }

  get modeIsEdit(): boolean {
    return this.mode === HorseCreateEditMode.edit;
  }


  private get modeActionFinished(): string {
    switch (this.mode) {
      case HorseCreateEditMode.create:
        return 'created';
      case HorseCreateEditMode.edit:
        return 'edit';
      default:
        return '?';
    }
  }

  ownerSuggestions = (input: string) => (input === '')
    ? of([])
    : this.ownerService.searchByName(input, 5);

  motherSuggestions = (input: string) => (input === '')
      ? of([])
      : this.service.searchByName(input, 5);

  fatherSuggestions = (input: string) => (input === '')
        ? of([])
        : this.service.searchByName(input, 5);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if(id!=null){
    this.id =Number(id);
    }

    this.route.data.subscribe(data => {
      this.mode = data.mode;
    });

    if(this.mode === HorseCreateEditMode.edit){
        this.loadHorse(this.id);
        }
     }


  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      // This names in this object are determined by the style library,
      // requiring it to follow TypeScript naming conventions does not make sense.
      // eslint-disable-next-line @typescript-eslint/naming-convention
      'is-invalid': !input.valid && !input.pristine,
    };
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

  deleteHorse(){
      this.service.deleteHorse(this.id).subscribe(
           (data) => {
               this.deletedHorse = this.horse.name;
               this.successDelete = true;
               console.log('Successfully deleted horse');
               this.notification.success('Horse successfully deleted. User will be redirected soon.');
                  setTimeout(() => {
                    this.successDelete = false;
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


  public onSubmit(form: NgForm): void {
    console.log('is form valid?', form.valid, this.horse);
    if (form.valid) {
      if (this.horse.description === '') {
        delete this.horse.description;
      }
      let observable: Observable<Horse>;
      switch (this.mode) {
        case HorseCreateEditMode.create:
          observable = this.service.create(this.horse);
          break;
        case HorseCreateEditMode.edit:
          observable = this.service.update(this.horse, this.id);
          break;
        default:
          console.error('Unknown HorseCreateEditMode', this.mode);
          return;
      }
      observable.subscribe({
        next: data => {
          this.notification.success(`Horse ${this.horse.name} successfully ${this.modeActionFinished}.`);
          this.router.navigate(['/horses']);
        },
        error: error => {
          console.error('Error creating horse', error);
                     const errorMessage = error.status === 0
                                       ? 'Is the backend up?'
                                       : error.message.message;
                                    this.notification.error(errorMessage, 'Could Not Create the Horse');
        }
      });
    }
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
         const errorMessage = error.status === 0
         ? 'Is the backend up?'
         : error.message.message;
         this.notification.error(errorMessage, 'Could Not Fetch Horses');
        }
      );
    }

}
