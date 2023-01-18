import {Component, OnInit} from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Observable, of} from 'rxjs';
import {Owner} from 'src/app/dto/owner';
import {OwnerService} from 'src/app/service/owner.service';

@Component({
  selector: 'app-owner-create',
  templateUrl: './owner-create.component.html',
  styleUrls: ['./owner-create.component.scss']
})
export class OwnerCreateComponent implements OnInit {

  owner: Owner = {
    firstName: '',
    lastName: '',
    email: '',
  };


  constructor(
    private ownerService: OwnerService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) {
  }

  ngOnInit(): void {}

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      // This names in this object are determined by the style library,
      // requiring it to follow TypeScript naming conventions does not make sense.
      // eslint-disable-next-line @typescript-eslint/naming-convention
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  public onSubmit(form: NgForm): void {
    console.log('is form valid?', form.valid, this.owner);
    if (form.valid) {
      if (this.owner.email === '') {
        delete this.owner.email;
      }
     this.ownerService.create(this.owner).subscribe({
        next: data => {
          this.notification.success(`Owner ${this.owner.firstName} successfully added.`);
          this.router.navigate(['/owners']);
        },
        error: error => {
          console.error('Error creating owner', error);
                               const errorMessage = error.status === 0
                                                 ? 'Is the backend up?'
                                                 : error.message.message;
                                              this.notification.error(errorMessage, 'Could Not Create the Owner');
        }
      });
    }
  }

}
