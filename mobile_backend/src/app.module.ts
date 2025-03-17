import { Module } from '@nestjs/common';
import { UserModule } from './user/user.module';
import { AuthModule } from './auth/auth.module';
import { PrismaModule } from './prisma/prisma.module';
import { EventModule } from './event/event.module';
import { VenueModule } from './venue/venue.module';
import { BookingModule } from './booking/booking.module';

@Module({
  imports: [AuthModule, UserModule, PrismaModule, EventModule, VenueModule, BookingModule],

})
export class AppModule {}
