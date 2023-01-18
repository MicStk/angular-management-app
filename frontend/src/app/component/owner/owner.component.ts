import {Component, OnInit} from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Owner} from 'src/app/dto/owner';
import {OwnerService} from 'src/app/service/owner.service';

@Component({
  selector: 'app-owner',
  templateUrl: './owner.component.html',
  styleUrls: ['./owner.component.scss']
})
export class OwnerComponent implements OnInit {
  search = false;
  owners: Owner[] = [];
  bannerError: string | null = null;

  constructor(
    private service: OwnerService,
    private notification: ToastrService,
  ) { }

  ngOnInit(): void {
    this.reloadOwners();
  }

  reloadOwners() {
    this.service.getAll()
      .subscribe({
        next: data => {
          this.owners = data;
        },
        error: error => {
          console.error('Error fetching owners', error);
          this.bannerError = 'Could not fetch owners: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Owners');
        }
      });
  }

}
